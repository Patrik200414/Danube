async function fetchPostAuthorizationFetch(url, token, postedObject){
    const postData = await fetch('/api/user/verify', {
        method: 'POST',
        headers: {
            'Content-type': 'Application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(postedObject)
    });

    return postData;
}

export default fetchPostAuthorizationFetch;