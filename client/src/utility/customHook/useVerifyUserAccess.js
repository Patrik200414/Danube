import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function useVerifyUserAccess(navigateUrl){
    const [isVerifed, setIsVerified] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const verifyUser = async () => {
            console.log('Verify');
            const userJwt = JSON.parse(sessionStorage.getItem('USER_JWT'))
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

        verifyUser();

    }, [navigateUrl, navigate])

    return isVerifed;
}

export default useVerifyUserAccess;