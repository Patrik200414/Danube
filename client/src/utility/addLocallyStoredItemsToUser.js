import fetchPostAuthorizationFetch from "./fetchPostAuthorizationFetch";

async function addLocallyStoredItemsToUser(currUser, locallyStoredItems){
    if(!locallyStoredItems.length){
        return;
    }

    const order = {
        customerId: currUser.id,
        products: locallyStoredItems.map(item => ({
            productId: item.id,
            quantity: item.orderedQuantity
        }))
    }

    const addLocallyStoredItems = await fetchPostAuthorizationFetch('/api/cart/integrate', currUser.jwt, JSON.stringify(order), true);    

}

export default addLocallyStoredItemsToUser;