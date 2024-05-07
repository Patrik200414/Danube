import fetchGetAuthorization from "./fetchGetAuthorization";

function getNavbarInformation(currUser){
    const navbarResponse = {userFirstName: null, cartItemNumber: 0};

    
    const storedCartItems = JSON.parse(localStorage.getItem('CART_ITEMS'));
    if(currUser){
        navbarResponse.userFirstName = currUser.firstName;
    }

    const getCartItemNumber = async (currUser) => {
        const cartItemData = await fetchGetAuthorization(`/api/cart/${Number(currUser.id)}`, currUser.jwt);
        const cartItemResponse = await cartItemData.json();
        navbarResponse.cartItemNumber = cartItemResponse.cartItems.length;
    }

    if(currUser){
        getCartItemNumber(currUser);
    } else if(storedCartItems){
        navbarResponse.cartItemNumber = storedCartItems.reduce((acc, curr) => acc + curr.orderedQuantity, 0);
    }
    return navbarResponse;
}

export default getNavbarInformation;