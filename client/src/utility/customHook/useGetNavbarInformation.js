import fetchGetAuthorization from "../fetchGetAuthorization";
import { useEffect, useState } from "react";

function useGetNavbarInformation(currUser){
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
            const cartItemData = await fetchGetAuthorization(`/api/cart/${Number(currUser.id)}`, currUser.jwt);
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


export default useGetNavbarInformation;