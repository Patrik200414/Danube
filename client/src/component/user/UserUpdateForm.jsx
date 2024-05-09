import PropTypes from 'prop-types';
import { useState, useEffect, useContext } from "react";
import { NavbarContext } from '../../NavbarContext';

function UserUpdateForm({onNavbarInformationChange}){
    const [user, setUser] = useState(JSON.parse(sessionStorage.getItem('USER_JWT')));
    const [isUpdateing, setIsUpdateing] = useState(false);
    const [error, setError] = useState();
    const [successFullUpload, setSuccessfullUpload] = useState(false);
    const [successMessage, setSuccessMessage] = useState();
    const navbarInfo = useContext(NavbarContext);
    

    const SUCCESS_MESSAGE_TIME_IN_SECONDS = 2;

    useEffect(() => {
        if(successFullUpload){
            let currTime = SUCCESS_MESSAGE_TIME_IN_SECONDS;
            setSuccessMessage('Your information has been updated!');
            const interval = setInterval(() => {
                if(currTime === 1){
                    clearInterval(interval);
                    setSuccessMessage('');
                    setSuccessfullUpload(false);
                }
                currTime--;
            }, 1000);
        }
    }, [successFullUpload]);

    async function handleSubmit(e){
        e.preventDefault();
        setIsUpdateing(true);

        const updatedUser = {
            email: user.email,
            firstName: user.firstName,
            lastName: user.lastName,
            userId: user.id
        };

        const updateUserData = await fetch(`/api/user/${user.id}`, {
            method: 'PUT',
            headers: {
                'Content-type': 'Application/json',
                'Authorization': `Bearer ${user.jwt}`
            },
            body: JSON.stringify(updatedUser)
        });

        const updateUserResponse = await updateUserData.json();
        if(updateUserData.ok){
            sessionStorage.setItem('USER_JWT', JSON.stringify(updateUserResponse));
            setUser(updateUserResponse);
            onNavbarInformationChange({
                userFirstName: updateUserResponse.firstName,
                cartItemNumber: navbarInfo.cartItemNumber
            });
            setSuccessfullUpload(true);
        } else{
            setError(updateUserResponse.errorMessage);
        }
        setIsUpdateing(false);
    }

    return(
        <>
            {successMessage && <h3 className="success-message">{successMessage}</h3>}
            <form className="user-update-form" onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="email">Email: </label>
                    <input onChange={e => setUser({...user, email: e.target.value})} type="text" id="email" name="email" placeholder="Email..." value={user.email}/>
                </div>

                <div>
                    <label htmlFor="firstName">First name: </label>
                    <input onChange={e => setUser({...user, firstName: e.target.value})} type="text" id="firstName" name="firstName" placeholder="First name..." value={user.firstName}/>
                </div>

                <div>
                    <label htmlFor="lastName">Last name: </label>
                    <input onChange={e => setUser({...user, lastName: e.target.value})} type="text" id="lastName" name="lastName" placeholder="Last name..." value={user.lastName}/>
                </div>

                <p className="error-message">{error}</p>
                <button disabled={isUpdateing}>Save</button>
            </form>
        </>
    )
}

UserUpdateForm.propTypes = {
    onNavbarInformationChange: PropTypes.func
}

export default UserUpdateForm;