async function fetchPatchAuthorizationFetch(url, token, body){
    const request = {
        method: "PATCH",
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }

    if(body){
        request.headers['Content-type'] = 'application/json';
        request.body = JSON.stringify(body);
    }

    const putData = await fetch(url, request);

    return putData;
}

export default fetchPatchAuthorizationFetch;