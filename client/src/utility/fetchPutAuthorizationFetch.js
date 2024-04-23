async function fetchPutAuthorizationFetch(url, token){
    const putData = await fetch(url, {
        method: "PUT",
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    return putData;
}

export default fetchPutAuthorizationFetch;