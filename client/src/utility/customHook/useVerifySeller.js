import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function useVerifySeller(){
    const [user, setUser] = useState();
    const navigate = useNavigate();

    useEffect(() => {
        const currUser = sessionStorage.getItem('USER_JWT');

        if(!currUser){
            navigate('/');
            return;
        }

        const userData = JSON.parse(currUser);
        if(!userData.roles.includes('ROLE_SELLER')){
            navigate('/');
            return
        }

        setUser(userData);
    }, [navigate]);

    return [user];
}


export default useVerifySeller;