import { useState } from "react";
import NavBar from "../component/NavBar";
import { useNavigate } from "react-router-dom";

function SellerAgreement(){
    const [user, setUser] = useState(JSON.parse(sessionStorage.getItem("USER_JWT")));
    const [isAgreementAccepted, setIsAgreementAccepted] = useState(false);
    const [error, setError] = useState('');

    const navigate = useNavigate();

    async function handleSubmit(){
        if(!isAgreementAccepted){
            setError('Seller agreement has to be accepted to become a seller!');
            return;
        }

        setError('');

        const updateRole = await fetch(`/api/user/${user.id}/role`, {
            method: "PUT",
            headers: {
                'Authorization': `Bearer ${user.jwt}`
            }
        });

        if(!updateRole.ok){
            setError(updateRole.statusText);
            return;
        }

        const updaterUser = await updateRole.json();
        sessionStorage.setItem('USER_JWT', JSON.stringify(updaterUser));
        navigate('/profile')

    }

    return(
        <div className="seller-agreement">
            <NavBar />
            <div className="agreement">
                <p className="emplanation-text">By proceeding to sell products on Danube&apos;s website, I, {user.firstName}, hereby acknowledge and agree to the following terms and conditions:</p>
                <ol className="agreement">
                    <li>I have read, understood, and agree to be bound by the terms and conditions of the Seller Agreement provided by Danube.</li>
                    <li>I will accurately list all products for sale on Danube&apos;s website, including product descriptions, images, pricing, and any other relevant information.</li>
                    <li>I acknowledge and agree that I have the legal right to sell the products listed on Danube&apos;s website, and that the sale of such products does not violate any laws or regulations.</li>
                    <li>I understand that Danube may charge fees for listing and selling products on its website, and I agree to pay any applicable fees in a timely manner.</li>
                    <li>I will fulfill all orders in a timely manner and provide accurate shipping and tracking information to Danube and the buyer.</li>
                    <li>I represent and warrant that I have all necessary rights, licenses, and permissions to use any intellectual property, including trademarks, logos, and copyrighted materials, in connection with the sale of products on Danube&apos;s website.</li>
                    <li>I agree to indemnify, defend, and hold harmless Danube, its officers, directors, employees, and agents, from and against any and all claims, liabilities, damages, losses, costs, and expenses arising out of or related to my breach of the Seller Agreement or violation of any law or third-party rights.</li>
                    <li>I understand that Danube may terminate this Agreement at any time, and upon termination, I will immediately cease listing and selling products on Danube&apos;s website.</li>
                </ol>
                <p className="emplanation-text">By clicking &quot;I Agree&quot; or by proceeding to sell products on Danube&apos;s website, I signify my acceptance of the terms and conditions set forth in this Seller Agreement.</p>
                <i className="parties">Danube</i>
                <br />
                <i className="parties">{user.firstName}</i>
                <br />
                <p className="error-message">{error}</p>
                <div className="agreement-checkbox">
                    <label htmlFor="checkbox">I agree the circumstances: </label>
                    <input type="checkbox" className="checkbox" id='checkbox' value={isAgreementAccepted} onChange={() => setIsAgreementAccepted(prev => !prev)}/>
                    <br />
                    <button type="button" onClick={handleSubmit}>Save changes!</button>
                </div>
            </div>
        </div>
    )
}

export default SellerAgreement;