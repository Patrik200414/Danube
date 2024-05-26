import { useEffect, useState } from "react";
import ProductsTable from "../component/product/ProductsTable";
import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";
import fetchGetAuthorization from "../utility/fetchGetAuthorization";
import ShippingInformationForm from "../component/order/ShippingInformationForm";

function Checkout(){
    const isVerified = useVerifyUserAccess('/verification/user/checkout', '/login');
    const [cartItems, setCartItems] = useState();
    const [shippingInformation, setShippingInformation] = useState({
        streetAddress: '',
        city: '',
        state: '',
        zip: '',
        country: ''
    })

    useEffect(() => {
        const getCartItems = async () => {
            const currUser = JSON.parse(sessionStorage.getItem("USER_JWT"));
            const productsResponse = await fetchGetAuthorization(`/api/cart/${currUser.id}`, currUser.jwt);
            const cartItemProducts = await productsResponse.json();

            setCartItems(cartItemProducts.cartItems);
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


    return(
        <div className="checkout-dashboard">
            {cartItems ? 
                <>
                    <h1>Checkout</h1>
                    <ProductsTable products={cartItems} buttons={[]}/>
                    <h3>Shipping information: </h3>
                    <ShippingInformationForm shippingInformation={shippingInformation} onShippingIformationChange={(informationKey, value) => handleShippingInformationChange(informationKey, value)}/>
                </>
                :
                <p className="loading-text">Loading...</p>
            }
            
        </div>
    )
}

export default Checkout;