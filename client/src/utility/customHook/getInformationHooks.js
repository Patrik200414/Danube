import { useEffect, useState } from "react";
import { fetchGetAuthorization } from "../fetchUtilities";

export function useGetCartItems(){
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

export function useGetNavbarInformation(currUser){
    const [navbarResponse, setNavbarResponse] = useState({userFirstName: null, cartItemNumber: 0});

    useEffect(() => {
        const storedCartItems = JSON.parse(localStorage.getItem('CART_ITEMS'));
        if(currUser){
            setNavbarResponse(prev => ({...prev, userFirstName: currUser.firstName}))
        }

        const calculateItemNumbers = (storedCartItems) => {
            return storedCartItems.reduce((acc, curr) => acc + curr.orderedQuantity, 0)
        }

        const getCartItemNumber = async (currUser) => {
            const cartItemData = await fetchGetAuthorization(`/api/cart/${currUser.id}`, currUser.jwt);
            const cartItemResponse = await cartItemData.json();
            const itemsCount = calculateItemNumbers(cartItemResponse.cartItems);
            setNavbarResponse(prev => ({...prev, cartItemNumber: itemsCount}))
        }


        if(currUser){
            getCartItemNumber(currUser);
        } else if(storedCartItems){
            setNavbarResponse(prev => ({...prev, cartItemNumber: calculateItemNumbers(storedCartItems)}))
        }
    }, []);
    

    return [navbarResponse, setNavbarResponse];
}