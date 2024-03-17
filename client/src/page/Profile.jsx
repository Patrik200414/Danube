import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import NavBar from "../component/NavBar";

import seller from "../static/seller.svg";
import settings from "../static/settings.svg"
import sellProduct from "../static/sellProduct.svg";

function Profile(){
    const [userRoles, setUserRoles] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const user = JSON.parse(sessionStorage.getItem('USER_JWT'));
        if(user){
            setUserRoles(user.roles);
        } else{
            navigate('/')
        }

    }, [])


    return(
        <div className="profile">
            <NavBar />
            {userRoles && 
            <>
                <div className="profile-services">
                    {!userRoles.includes("ROLE_SELLER") && 
                        <div className="service">
                            <Link to='/seller/agreement'>
                                <img src={seller} />
                                <h2>Become a seller</h2>
                                <p>Welcome to our seller service, designed to empower entrepreneursto thrive in the digital marketplace. Our platform provides a seamless and efficient way for sellers to showcase their products, connect with customers, and grow their businesses online.</p>
                            </Link>
                        </div>
                    }
                    {userRoles.includes("ROLE_SELLER") &&
                        <div className="service">
                            <Link to='/seller/upload'>
                                <img src={sellProduct} />
                                <h2>Upload product</h2>
                                <p>This service offers a comprehensive set of features and functionalities to empower sellers in showcasing their products effectively and maximizing sales potential.</p>
                            </Link>
                        </div>
                    }

                    <div className="service">
                        <Link to='/profile/settings'>
                            <img src={settings} />
                            <h2>Profile settings</h2>
                            <p>The Profile Setting Service is a comprehensive feature designed to empower users with control over their personal accounts within the platform. It offers a range of customization options and management tools to enhance user experience and privacy.</p>
                        </Link>
                    </div>
                </div>
            </>
            }
        </div>
    )
}

export default Profile;