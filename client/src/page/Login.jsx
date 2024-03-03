import { useState } from "react";
import { useNavigate } from "react-router";
import { Link } from "react-router-dom";

function Login(){
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

        const loginResponseData = await fetch('/api/user/login', {
            method: 'POST',
            headers: {
                'Content-type': 'Application/json'
            },
            body: JSON.stringify(userLogin)
        });

        const loginResponse = await loginResponseData.json();

        if(loginResponseData.ok){
            sessionStorage.setItem('USER_JWT', JSON.stringify(loginResponse));
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
            
                <label htmlFor="email">Email: </label>
                <input type="text" name="email" id="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)}/>
                <br />

                <label htmlFor="password">Password: </label>
                <input type="password" name="password" id="password" placeholder="Passsword" value={password} onChange={e => setPassword(e.target.value)}/>
                <br />

                <p className="error-message">{error}</p>

                <button>Login</button>
                <Link to='/registration'>Create account!</Link>

            </form>
        </div>
    )
}

export default Login;