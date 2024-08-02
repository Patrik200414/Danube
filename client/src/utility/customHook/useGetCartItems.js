import { useEffect, useState } from "react";
import { fetchGetAuthorization } from "../fetchUtilities";

function useGetCartItems(){
    const [cartItems, setCartItems] = useState();
    const [error, setError] = useState();

    useEffect(() => {
        const controller = new AbortController();
        const currUser = JSON.parse(sessionStorage.getItem('USER_JWT'));

        const getCartElementForRegisteredUser = async (currUser) => {
            const cartData = await fetchGetAuthorization(`/api/cart/${currUser.id}`, currUser.jwt);
            const cartResponse = await cartData.json();
            if(cartData.ok){
                setCartItems(cartResponse.cartItems);
                setError();
            } else{
                setError(cartResponse.errorMessage);
            }
        }

        

        const locallyStoredItems = JSON.parse(localStorage.getItem('CART_ITEMS'));
        if(currUser){
            getCartElementForRegisteredUser(currUser);
        } else if(locallyStoredItems){
            setCartItems(locallyStoredItems);
        } else {
            setCartItems([]);
        }

        return () => controller.abort();
    }, [])

    return [cartItems, setCartItems, error]
}

export default useGetCartItems;