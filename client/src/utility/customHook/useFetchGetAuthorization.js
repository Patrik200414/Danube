import { useEffect, useState } from "react";

function useFetchGetAuthorization(url, user){
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

export default useFetchGetAuthorization;