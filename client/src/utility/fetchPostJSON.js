async function fetchPostJSON(url, postedObject){
    const getData = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-type': 'Application/json'
        },
        body: JSON.stringify(postedObject)
    });

    return getData;
}

export default fetchPostJSON;