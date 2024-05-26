import { useEffect, useState } from "react";
import ProductsTable from "../component/product/ProductsTable";
import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";
import fetchGetAuthorization from "../utility/fetchGetAuthorization";

function Checkout(){
    const isVerified = useVerifyUserAccess('/verification/user/checkout', '/login');
    const [cartItems, setCartItems] = useState();

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


    return(
        <div className="checkout-dashboard">
            {cartItems ? 
                <>
                    <h1>Checkout</h1>
                    <ProductsTable products={cartItems} buttons={[]}/>
                </>
                :
                <p className="loading-text">Loading...</p>
            }
            
        </div>
    )
}

export default Checkout;