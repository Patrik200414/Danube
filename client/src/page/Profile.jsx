import { Link } from "react-router-dom";
import NavBar from "../component/NavBar";
import seller from "../static/seller.svg";
import settings from "../static/settings.svg"

function Profile(){
    return(
        <div className="profile">
            <NavBar />
            <div className="profile-services">
                <div className="service">
                    <Link to='/seller/agreement'>
                        <img src={seller} />
                        <h2>Become a seller</h2>
                        <p>Welcome to our seller service, designed to empower entrepreneursto thrive in the digital marketplace. Our platform provides a seamless and efficient way for sellers to showcase their products, connect with customers, and grow their businesses online.</p>
                    </Link>
                </div>

                <div className="service">
                    <Link to='/profile/settings'>
                        <img src={settings} />
                        <h2>Profile settings</h2>
                        <p>The Profile Setting Service is a comprehensive feature designed to empower users with control over their personal accounts within the platform. It offers a range of customization options and management tools to enhance user experience and privacy.</p>
                    </Link>
                </div>
            </div>
        </div>
    )
}

export default Profile;