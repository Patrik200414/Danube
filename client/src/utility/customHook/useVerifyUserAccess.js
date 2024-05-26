import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function useVerifyUserAccess(navigateUrl, fallBackUrl){
    const [isVerifed, setIsVerified] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const verifyUser = async (userJwt) => {
            const verifyData = await fetch('/api/user/verify', {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${userJwt.jwt}`
                },
                body: JSON.stringify({token: userJwt.jwt})
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

    }, [navigateUrl, navigate])

    return isVerifed;
}

export default useVerifyUserAccess;