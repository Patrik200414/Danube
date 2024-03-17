import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";


function Verification(){
    const [user, setUser] = useState();
    const navigate = useNavigate();
    
    useEffect(() => {
        const urlLength = window.location.href.split('/').length;
        const verificationBy = window.location.href.split('/')[urlLength - 1];

        if(verificationBy){
            const userData = JSON.parse(sessionStorage.getItem("USER_JWT"));
    
            if(!userData){
                navigate('/');
                return;
            }
    
            if(verificationBy === 'seller' && !userData.roles.includes('ROLE_SELLER')){
                navigate('/profile');
                return;
            }
    
            setUser(userData);
        } else{
            navigate('/');
        }

    }, []);

    return(
        
        <div className="verification">
            {user &&
                <h1>Verification</h1>        
            }
        </div>
    )
}

export default Verification;