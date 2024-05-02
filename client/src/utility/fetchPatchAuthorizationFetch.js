async function fetchPatchAuthorizationFetch(url, token){
    const putData = await fetch(url, {
        method: "PATCH",
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    return putData;
}

export default fetchPatchAuthorizationFetch;