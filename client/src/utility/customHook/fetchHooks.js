import { useEffect, useState } from "react";

export function useFetch(url){
    const [data, setData] = useState();
    const [error, setError] = useState();

    useEffect(() => {
        const getData = async() => {
            const responseData = await fetch(url);
            const response = await responseData.json();
            if(responseData.ok){
                setData(response);
            } else{
                setError(response.errorMessage);
            }

        }

        getData();
    }, [url]);


    return [data, setData, error];
}

export function useFetchGetAuthorization(url, user){
    const [data, setData] = useState();
    const [error, setError] = useState();

    useEffect(() => {
        const getData = async() => {
            const responseData = await fetch(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${user.jwt}`
                }
            });
            const response = await responseData.json();

            if(responseData.ok){
                setData(response);
            } else{
                setError(response.errorMessage);
            }
        }

        if(user){
            getData();
        }
    }, [url, user]);

    return [data, setData, error];
}