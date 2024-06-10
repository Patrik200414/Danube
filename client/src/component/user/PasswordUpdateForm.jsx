import { useState } from "react";
import Proptypes from 'prop-types';

function PasswordUpdateForm({user, navigate}){
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [reenterPassword, setReenterPassword] = useState('');
    const [error, setError] = useState('');


    async function handleSubmit(e){
        e.preventDefault();

        const updatedPassword = {
            currentPassword: currentPassword,
            newPassword: newPassword,
            reenterPassword: reenterPassword,
        };

        
        const passwordUpdateData = await fetch(`/api/user/password/${user.id}`, {
            method: 'PUT',
            headers: {
                'Content-type': 'Application/json',
                'Authorization': `Bearer ${user.jwt}`
            },
            body: JSON.stringify(updatedPassword)
        });

        
        if(passwordUpdateData.ok){
            navigate('/user/update');
            return;
        }
        const passwordUpdateResponse = await passwordUpdateData.json();
        setError(passwordUpdateResponse.errorMessage);
    }

    return(
        <form className="user-update-form" onSubmit={handleSubmit}>
            <div>
                <label htmlFor="currentPassword">Current password: </label>
                <input onChange={e => setCurrentPassword(e.target.value)} type="password" id="currentPassword" name="currentPassword" value={currentPassword}/>
            </div>

            <div>
                <label htmlFor="newPassword">New password: </label>
                <input onChange={e => setNewPassword(e.target.value)} type="password" id="newPassword" name="newPassword" value={newPassword}/>
            </div>

            <div>
                <label htmlFor="reenterNewPassword">Reenter new password: </label>
                <input onChange={e => setReenterPassword(e.target.value)} type="password" id="reenterNewPassword" name="reenterNewPassword" value={reenterPassword}/>
            </div>

            <p className="error-message">{error}</p>
            <button>Save</button>
        </form>
    )
}


PasswordUpdateForm.propTypes = {
    user: Proptypes.object,
    navigate: Proptypes.func
}
export default PasswordUpdateForm;