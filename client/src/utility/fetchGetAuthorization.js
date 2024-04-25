async function fetchGetAuthorization(url, token){
    const detailsData = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    return detailsData;
}

export default fetchGetAuthorization;