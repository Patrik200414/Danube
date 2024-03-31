import { useState } from "react";

function UserUpdateForm(){
    const [user, setUser] = useState(JSON.parse(sessionStorage.getItem('USER_JWT')));
    console.log(user);

    function handleSubmit(e){
        e.preventDefault();
    }

    return(
        <form className="user-update-form" onSubmit={handleSubmit}>
            <label htmlFor="email">Email: </label>
            <input onChange={e => setUser({...user, email: e.target.value})} type="text" id="email" name="email" placeholder="Email..." value={user.email}/>
            <br />

            <label htmlFor="firstName">First name: </label>
            <input onChange={e => setUser({...user, firstName: e.target.value})} type="text" id="firstName" name="firstName" placeholder="First name..." value={user.firstName}/>
            <br />

            <label htmlFor="lastName">Last name: </label>
            <input onChange={e => setUser({...user, lastName: e.target.value})} type="text" id="lastName" name="lastName" placeholder="Last name..." value={user.lastName}/>
            <br />

            <button>Save</button>
        </form>
    )
}

export default UserUpdateForm;