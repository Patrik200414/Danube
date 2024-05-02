import { useEffect, useState } from "react";

function useFetch(url){
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

export default useFetch;