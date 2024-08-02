import { fetchPostAuthorizationFetch } from "./fetchUtilities";


export async function addLocallyStoredItemsToUser(currUser, locallyStoredItems){
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

    await fetchPostAuthorizationFetch('/api/cart/integrate', currUser.jwt, JSON.stringify(order), true);    
}

export function appendFilesToFormData(fileName, files){
    const formData = new FormData();
    for(let i = 0; i < files.length; i++){
        formData.append(fileName, files[i]);
    }

    return formData;
}

export function buttonObjectGenerator(actionName, buttonText, isDynamic, linkTo, onClick){
    return {
        actionName, buttonText, isDynamic, linkTo, onClick
    }
}

export function changeProductDetail(value, detailId, details){
    const changedDetails = details.map(detail => {
        if(detail.id === detailId){
            return{
                ...detail,
                value: value
            }
        }
        return detail;
    });
    return changedDetails;
}

export async function handlePromiseAllError(promiseAllResponse, callbackFunction){
    const resultErrors = [];
    for(let i = 0; i < promiseAllResponse.length; i++){
        if(!promiseAllResponse[i].ok){
            const errorResponse = await promiseAllResponse[i].json();
            if(!resultErrors.includes(errorResponse)){
                resultErrors.push(errorResponse.errorMessage);
            }
        }
    }
    if(resultErrors.length){
        callbackFunction(resultErrors);
        return false;
    }

    return true;
}