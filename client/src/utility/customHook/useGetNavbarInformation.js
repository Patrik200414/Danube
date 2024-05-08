import fetchGetAuthorization from "../fetchGetAuthorization";
import addLocallyStoredItemsToUser from "../addLocallyStoredItemsToUser";
import { useEffect, useState } from "react";

function useGetNavbarInformation(currUser){
    const [navbarResponse, setNavbarResponse] = useState({userFirstName: null, cartItemNumber: 0});

    useEffect(() => {
        const storedCartItems = JSON.parse(localStorage.getItem('CART_ITEMS'));
        if(currUser){
            setNavbarResponse(prev => ({...prev, userFirstName: currUser.firstName}))
        }

        const getCartItemNumber = async (currUser) => {
            const cartItemData = await fetchGetAuthorization(`/api/cart/${Number(currUser.id)}`, currUser.jwt);
            const cartItemResponse = await cartItemData.json();
            setNavbarResponse(prev => ({...prev, cartItemNumber: cartItemResponse.cartItems.length}))
        }

        if(currUser){
            getCartItemNumber(currUser);
            if(storedCartItems){
                /* const itemResponse = addLocallyStoredItemsToUser(currUser, storedCartItems); */
            }
        } else if(storedCartItems){
            navbarResponse.cartItemNumber = storedCartItems.reduce((acc, curr) => acc + curr.orderedQuantity, 0);
        }
    }, []);
    

    return [navbarResponse, setNavbarResponse];
}


export default useGetNavbarInformation;