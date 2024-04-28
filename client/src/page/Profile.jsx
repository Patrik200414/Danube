import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

import seller from "../static/seller.svg";
import settings from "../static/settings.svg"
import sellProduct from "../static/sellProduct.svg";
import updateProduct from "../static/updateProduct.svg";

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

    }, [navigate])


    return(
        <div className="profile">
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
                        <>
                            <div className="service">
                                <Link to='/verification/product/upload'>
                                    <img src={sellProduct} />
                                    <h2>Upload product</h2>
                                    <p>This service offers a comprehensive set of features and functionalities to empower sellers in showcasing their products effectively and maximizing sales potential.</p>
                                </Link>
                            </div>
                            <div className="service">
                                <Link to='/verification/product/update'>
                                    <img src={updateProduct} />
                                    <h2>Update product</h2>
                                    <p>Unlock a suite of robust features designed to elevate your online storefront. Seamlessly update product details, upload fresh images, and refine descriptions with ease. Stay ahead in the market by showcasing your inventory in its best light and optimizing sales opportunities effortlessly.</p>
                                </Link>
                            </div>
                        </>
                    }

                    <div className="service">
                        <Link to='/verification/user/update'>
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