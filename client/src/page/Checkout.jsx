import { useEffect, useState } from "react";
import ProductsTable from "../component/product/ProductsTable";
import useFetchGetAuthorization from "../utility/customHook/useFetchGetAuthorization";
import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";
import fetchGetAuthorization from "../utility/fetchGetAuthorization";

function Checkout(){
    const isVerified = useVerifyUserAccess('/verification/user/checkout', '/login');
    const [cartItems, setCartItems] = useState();

    useEffect(() => {
        const getCartItems = async () => {
            const currUser = JSON.parse(sessionStorage.getItem("USER_JWT"));
            const products = await fetchGetAuthorization(`/api/cart/${currUser.id}`, currUser.jwt);

            console.log(products);
        };

        if(isVerified){
            getCartItems();
        }
    }, [isVerified])


    return(
        <div className="checkout-dashboard">
            <h1>Checkout</h1>
            {/* <ProductsTable /> */}
        </div>
    )
}

export default Checkout;