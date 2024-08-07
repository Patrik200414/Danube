import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import PropTypes from "prop-types";
import UserAccountInformation from "../component/user/UserAccountInformation";
import { fetchPostAuthorizationFetch } from "../utility/fetchUtilities";
import ErrorMessage from "../component/error/ErrorMessage/ErrorMessage";


function Verification({verificationToPages}){
    const [user, setUser] = useState();
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const {verificationBy, to} = useParams()
    const targetUrl = `/${verificationBy}/${to}`;


    useEffect(() => {
        if(verificationBy && verificationToPages.includes(to)){
            const userData = JSON.parse(sessionStorage.getItem("USER_JWT"));
    
            if(!userData){
                navigate('/');
                return;
            }
    
            if(verificationBy === 'product' && !userData.roles.includes('ROLE_SELLER')){
                navigate('/profile');
                return;
            }

            
            setUser(userData);
        } else{
            navigate('/');
        }

    }, [verificationToPages, navigate, to, verificationBy]);

    async function handleSubmit(e){
        e.preventDefault();

        if(!password){
            setError('Missing field!');
        }

        const userVerification = {
            email: user.email,
            password: password
        };

        const verificationResult = await fetchPostAuthorizationFetch('/api/user/authenticate', user.jwt, JSON.stringify(userVerification), true);
        const verificationResponse = await verificationResult.json();


        if(verificationResult.ok){
            sessionStorage.setItem('USER_JWT', JSON.stringify(verificationResponse));
            navigate(targetUrl);
        } else{
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
                        {error && <ErrorMessage error={error}/>}
                        <button>Verify</button>
                    </form>
                </>
            }
        </div>
    )
}

Verification.propTypes = {
    verificationToPages: PropTypes.array
}

export default Verification;