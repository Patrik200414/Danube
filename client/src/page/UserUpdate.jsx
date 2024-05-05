import PropTypes from 'prop-types';
import { Link } from "react-router-dom";
import UserUpdateForm from "../component/user/UserUpdateForm";
import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";

function UserUpdate({onNavbarInformationChange}){
    const isAccessible = useVerifyUserAccess('/verification/user/update');

    return(
        <div className="user-update-container">
            {isAccessible && 
                <>
                    <UserUpdateForm onNavbarInformationChange={(information) => onNavbarInformationChange(information)}/>
                    <Link className="change-password" to={'/user/password'}>
                        <button>Change password</button>
                    </Link>
                </>
            }
            
        </div>
    )
}

UserUpdate.propTypes = {
    onNavbarInformationChange: PropTypes.func
}

export default UserUpdate;