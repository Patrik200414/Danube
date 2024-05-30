async function handlePromiseAllError(promiseAllResponse, callbackFunction){
    const resultErrors = [];
    for(let i = 0; i < promiseAllResponse.length; i++){
        if(!promiseAllResponse[i].ok){
            const errorResponse = await promiseAllResponse[i].json();
            if(!resultErrors.includes(errorResponse)){
                resultErrors.push(errorResponse.errorMessage);
            }
        }
    }
    console.log(resultErrors);
    if(resultErrors.length){
        callbackFunction(resultErrors);
        return false;
    }

    return true;
}

export default handlePromiseAllError;