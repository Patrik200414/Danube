async function fetchDeleteAuthorization(url, token){
    const deleteResponse = await fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })

    return deleteResponse;
}

export default fetchDeleteAuthorization;