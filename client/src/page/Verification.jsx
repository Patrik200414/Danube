import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import UserAccountInformation from "../component/UserAccountInformation";


function Verification(){
    const [user, setUser] = useState();
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
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

    async function handleSubmit(e){
        e.preventDefault();

        if(!password){
            setError('Missing field!');
        }

        const userVerification = {
            email: user.email,
            password: password
        };

        const verificationResult = await fetch('/api/user/verify', {
            method: 'POST',
            headers: {
                'Content-type': 'Application/json',
                'Authorization': `Bearer ${user.jwt}`
            },
            body: JSON.stringify(userVerification)
        });

        if(verificationResult.ok){
            navigate('/product/upload');
        } else{
            const verificationResponse = await verificationResult.json();
            setError(verificationResponse.errorMessage);
        }

    }

    return(
        
        <div className="verification login-container">
            {user &&
                <>
                    <h1 className="verification-title">Verification</h1>
                    <UserAccountInformation user={user}/>
                    <form className="login-form" onSubmit={handleSubmit}>
                        <label htmlFor="password">Password: </label>
                        <br />
                        <input onChange={(e) => setPassword(e.target.value)} type="password" id="password" name="password" className="password" value={password}/>
                        <br />
                        {error && <p className="error-message">{error}</p>}
                        <button>Verify</button>
                    </form>
                </>
            }
        </div>
    )
}

export default Verification;