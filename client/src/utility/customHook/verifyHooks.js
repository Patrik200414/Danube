import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export function useVerifyUserAccess(navigateUrl, fallBackUrl){
    const [isVerifed, setIsVerified] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const verifyUser = async (userJwt) => {
            const verifyData = await fetch('/api/user/verify', {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${userJwt.jwt}`
                }
            });
            
            if(!verifyData.ok){
                navigate(navigateUrl);
                return 
            }
            setIsVerified(true);
        }

        const currUser = JSON.parse(sessionStorage.getItem('USER_JWT'));
        if(currUser){
            verifyUser(currUser);
        } else{
            navigate(fallBackUrl);
        }

    }, [navigateUrl, fallBackUrl, navigate])

    return isVerifed;
}

export function useVerifyUserRole(role){
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
