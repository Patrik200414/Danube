import { useEffect, useState } from "react";
import ProductsTable from "../component/product/ProductsTable";
import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";
import fetchGetAuthorization from "../utility/fetchGetAuthorization";
import ShippingInformationForm from "../component/order/ShippingInformationForm";
import fetchPostAuthorizationFetch from "../utility/fetchPostAuthorizationFetch";

function Checkout(){
    const isVerified = useVerifyUserAccess('/verification/user/checkout', '/login');
    const [user, setUser] = useState();
    const [cartItems, setCartItems] = useState();
    const [shippingInformation, setShippingInformation] = useState({
        streetAddress: '',
        city: '',
        state: '',
        zip: '',
        country: ''
    })
    const [error, setError] = useState();
    const [paymentUrl, setPaymentUrl] = useState();

    useEffect(() => {
        if(paymentUrl){
            window.location.href = paymentUrl;
        }
    }, [paymentUrl])

    useEffect(() => {
        const getCartItems = async () => {
            const currUser = JSON.parse(sessionStorage.getItem("USER_JWT"));
            const productsResponse = await fetchGetAuthorization(`/api/cart/${currUser.id}`, currUser.jwt);
            const cartItemProducts = await productsResponse.json();

            setCartItems(cartItemProducts.cartItems);
            setUser(currUser);
        };

        if(isVerified){
            getCartItems();
        }
    }, [isVerified])

    function handleShippingInformationChange(informationKey, value){
        setShippingInformation(prev => ({
            ...prev,
            [informationKey]: value
        }));
    }

    function validateAddressInformation(addressInformation){
        const missingFields = Object.keys(addressInformation).filter(key => !addressInformation[key]);

        if(missingFields.length){
            return `Missing values at: ${missingFields.join(', ')}`;
        }

        
        if(addressInformation.zip.length < 4){
            return 'Invalid Zip/Postal code!';
        }
    }


    async function handleProceedPayout(e){
        e.preventDefault();

        const errorMessage = validateAddressInformation(shippingInformation);
        if(errorMessage){
            setError(errorMessage);
            return;
        }

        const userShippingInformation = {
            ...shippingInformation,
            customerId: user.id
        }

        const paymentData = await fetchPostAuthorizationFetch(`/api/payment`, user.jwt, JSON.stringify({userId: user.id}), true);
        const paymentResponse = await paymentData.json();

        if(paymentData.ok){
            setPaymentUrl(paymentResponse.paymentUrl);
        } else{
            setError(paymentResponse.message);
        }
        
    }

    return(
        <div className="checkout-dashboard">
            {cartItems ? 
                <>
                    <h1>Checkout</h1>
                    <ProductsTable products={cartItems} buttons={[]}/>
                    <h3>Shipping information: </h3>
                    <ShippingInformationForm shippingInformation={shippingInformation} onShippingIformationChange={(informationKey, value) => handleShippingInformationChange(informationKey, value)}/>
                    <p className="error-message">{error}</p>
                    <button className="submit-button" onClick={handleProceedPayout}>Proceed Payout</button>
                </>
                :
                <p className="loading-text">Loading...</p>
            }
            
        </div>
    )
}

export default Checkout;