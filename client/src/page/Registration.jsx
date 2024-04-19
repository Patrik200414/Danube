import { useState } from "react";
import { useNavigate } from "react-router";
import { Link } from "react-router-dom";

function Registration(){
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [reEnterPassword, setReEnterPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    async function handleSubmit(e){
        e.preventDefault();

        if(password !== reEnterPassword){
            setError('Password and the re-entered password should be same!');
            return
        }

        const newUser = {
            firstName,
            lastName,
            email,
            password
        };

        const registrationResponseData = await fetch('/api/user/registration', {
            method: 'POST',
            headers: {
                'Content-type': 'Application/json'
            },
            body: JSON.stringify(newUser)
        });

        

        if(registrationResponseData.ok){
            navigate('/login');
        } else{
            const registrationResponse = await registrationResponseData.json();
            setError(registrationResponse.errorMessage);
        }
    }

    return(
        <div className="registration-container">
            <h1>Danube</h1>
            <h3>Registration</h3>
            <form className="registration-form" onSubmit={handleSubmit}>

                <label htmlFor="firstName">First name: </label>
                <input type="text" name="firstName" id="firstName" placeholder="First name" value={firstName} onChange={e => setFirstName(e.target.value)}/>
                <br />
            
                <label htmlFor="lastName">Last name: </label>
                <input type="text" name="lastName" id="lastName" placeholder="Last name" value={lastName} onChange={e => setLastName(e.target.value)}/>
                <br />

                <label htmlFor="email">Email: </label>
                <input type="text" name="email" id="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)}/>
                <br />

                <label htmlFor="password">Password: </label>
                <input type="password" name="password" id="password" placeholder="Passsword" value={password} onChange={e => setPassword(e.target.value)}/>
                <i>Password should be at least 6 characters!</i>
                <br />

                <label htmlFor="reEnterPassword">Re-enter password: </label>
                <input type="password" name="reEnterPassword" id="reEnterPassword" placeholder="Re-enter password" value={reEnterPassword} onChange={e => setReEnterPassword(e.target.value)}/>
                <br />

                <p className="error-message">{error}</p>

                <button>Registrate</button>
                <Link to='/login'>Go to login!</Link>

            </form>
        </div>
    )
}

export default Registration;