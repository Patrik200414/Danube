export async function fetchDeleteAuthorization(url, token){
    const deleteResponse = await fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })

    return deleteResponse;
}

export async function fetchGet(url){
    const getRequest = await fetch(url);
    const getResponse = await getRequest.json();
    return getResponse;    
}

export async function fetchGetAuthorization(url, token){
    const detailsData = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    return detailsData;
}

export async function fetchPatchAuthorizationFetch(url, token, body){
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

export async function fetchPostAuthorizationFetch(url, token, postedObject, isJson){
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

export async function fetchPostJSON(url, postedObject){
    const getData = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-type': 'Application/json'
        },
        body: JSON.stringify(postedObject)
    });

    return getData;
}
