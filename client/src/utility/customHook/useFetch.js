import { useEffect, useState } from "react";

function useFetch(url){
    const [data, setData] = useState();

    useEffect(() => {
        const getData = async() => {
            const responseData = await fetch(url);
            const response = await responseData.json();

            setData(response);
        }

        getData();
    }, [url]);


    return [data];
}

export default useFetch;