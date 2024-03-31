import { useState } from "react";

function PasswordUpdateForm(){
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [reenterPassword, setReenterPassword] = useState('');
    const [error, setError] = useState('');


    function handleSubmit(e){
        e.preventDefault();

        if(newPassword !== reenterPassword){
            setError('The password and the reenter password are not the same!');
            return;
        }

        const updatedPassword = {
            currentPassword: currentPassword,
            newPassword: newPassword,
            reenterPassword: reenterPassword
        };

        
    }

    return(
        <form className="user-update-form" onSubmit={handleSubmit}>
            <label htmlFor="currentPassword">Current password: </label>
            <input onChange={e => setCurrentPassword(e.target.value)} type="password" id="currentPassword" name="currentPassword" value={currentPassword}/>
            <br />

            <label htmlFor="newPassword">New password: </label>
            <input onChange={e => setNewPassword(e.target.value)} type="password" id="newPassword" name="newPassword" value={newPassword}/>
            <br />

            <label htmlFor="reenterNewPassword">Reenter new password: </label>
            <input onChange={e => setReenterPassword(e.target.value)} type="password" id="reenterNewPassword" name="reenterNewPassword" value={reenterPassword}/>
            <br />

            <button>Save</button>
        </form>
    )
}

export default PasswordUpdateForm;