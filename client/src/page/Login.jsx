import PropTypes from "prop-types";
import { useState } from "react";
import { useNavigate } from "react-router";
import { Link } from "react-router-dom";
import fetchPostJSON from "../utility/fetchPostJSON";
import fetchGetAuthorization from "../utility/fetchGetAuthorization";

function Login({onNavbarInformationChange}){
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();


    async function handleSubmit(e){
        e.preventDefault();


        const userLogin = {
            email,
            password
        };


        const loginResponseData = await fetchPostJSON('/api/user/login', userLogin)
        const loginResponse = await loginResponseData.json();

        if(loginResponseData.ok){
            sessionStorage.setItem('USER_JWT', JSON.stringify(loginResponse));
            const cartItemsData = await fetchGetAuthorization(`/api/cart/${loginResponse.id}`, loginResponse.jwt);
            const cartItemsResponse = await cartItemsData.json();
            onNavbarInformationChange({
                userFirstName: loginResponse.firstName,
                cartItemNumber: cartItemsResponse ? cartItemsResponse.cartItems.length : -1
            });
            navigate('/');
        } else {
            setError(loginResponse.errorMessage);
        }
    
    }
    

    return(
        <div className="login-container">
            <h1>Danube</h1>
            <h3>Sign in</h3>
            <form className="login-form" onSubmit={handleSubmit}>
            
                <div>
                    <label htmlFor="email">Email: </label>
                    <input type="text" name="email" id="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)}/>
                </div>

                <div>
                    <label htmlFor="password">Password: </label>
                    <input type="password" name="password" id="password" placeholder="Passsword" value={password} onChange={e => setPassword(e.target.value)}/>
                </div>

                <p className="error-message">{error}</p>

                <button>Login</button>
                <Link to='/registration'>Create account!</Link>

            </form>
        </div>
    )
}

Login.propTypes = {
    onNavbarInformationChange: PropTypes.func
}

export default Login;