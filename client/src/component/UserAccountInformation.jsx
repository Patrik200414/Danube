import userImage from "../static/userImage.svg";
import Proptypes from "prop-types"

function UserAccountInformation({user}){

    return(
        <div className="user-account-information">
            {user && 
            <>
                <img src={userImage} />
                <div className="user-details">
                    <p>{user.firstName} {user.lastName}</p>
                    <span>{user.email}</span>
                </div>
            </>
            }
        </div>
    )
}


UserAccountInformation.propTypes  = {
    user: Proptypes.object
}
export default UserAccountInformation;