async function fetchPostAuthorizationFetch(url, token, postedObject, isJson){
    const headerParameters = {
        'Authorization': `Bearer ${token}`
    }

    if(isJson){
        headerParameters['Content-type'] = 'application/json';
    }

    const postData = await fetch(url, {
        method: 'POST',
        headers: headerParameters,
        body: postedObject
    });

    return postData;
}

export default fetchPostAuthorizationFetch;