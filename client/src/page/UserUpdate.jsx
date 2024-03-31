import NavBar from "../component/NavBar";
import UserUpdateForm from "../component/UserUpdateForm";

function UserUpdate(){

    return(
        <div className="user-update-container">
            <NavBar />
            <UserUpdateForm />
        </div>
    )
}

export default UserUpdate;