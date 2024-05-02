import { useEffect, useState } from "react";

function useFetchGetAuthorization(url, user){
    const [data, setData] = useState();

    useEffect(() => {
        const getData = async() => {
            const responseData = await fetch(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${user.jwt}`
                }
            });
            const response = await responseData.json();

            setData(response);
        }

        if(user){
            getData();
        }
    }, [url, user]);


    return [data, setData];
}

export default useFetchGetAuthorization;