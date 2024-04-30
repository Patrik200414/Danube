import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function useVerifyUser(role){
    const [user, setUser] = useState();
    const navigate = useNavigate();

    useEffect(() => {
        const currUser = sessionStorage.getItem('USER_JWT');

        if(!currUser){
            navigate('/');
            return;
        }

        const userData = JSON.parse(currUser);
        if(!userData.roles.includes(role)){
            navigate('/');
            return
        }

        setUser(userData);
    }, [navigate, role]);

    return [user];
}


export default useVerifyUser;