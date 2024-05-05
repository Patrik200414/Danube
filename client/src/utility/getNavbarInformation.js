import fetchGetAuthorization from "./fetchGetAuthorization";

function getNavbarInformation(){
    const navbarResponse = {userFirstName: null, cartItems: []};

    const currUser = JSON.parse(sessionStorage.getItem('USER_JWT'));
    console.log(currUser);
    const storedCartItems = JSON.parse(localStorage.getItem('CART_ITEMS'));
    if(currUser){
        navbarResponse.userFirstName = currUser.firstName;
    }

    const getCartItems = async (currUser) => {
        
        const cartItemResponse = await fetchGetAuthorization(`/api/cart/${Number(currUser.id)}`, currUser.jwt);
        navbarResponse.cartItems = cartItemResponse.cartItems;
    }

    if(currUser){
        getCartItems(currUser);
    } else if(storedCartItems){
        navbarResponse.cartItems = storedCartItems;
    }

    return navbarResponse;
}

export default getNavbarInformation;