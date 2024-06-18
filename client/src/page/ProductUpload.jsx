import { useEffect, useState } from "react";
import ProductInformationForm from "../component/product/ProductInformationForm"
import { useNavigate } from "react-router-dom";
import ProductCategoryForm from "../component/product/ProductCategoryForm";
import ProductDetailsForm from "../component/product/ProductDetailsForm";
import UploadedImages from "../component/product/UploadedImages";
import fetchPostAuthorizationFetch from '../utility/fetchPostAuthorizationFetch';
import changeProductDetail from '../utility/changeProductDetail';
import imageUpload from '../utility/imageUpload';
import useVerifyUserRole from "../utility/customHook/useVerifyUserRole";
import appendFilesToFormData from '../utility/appendFilesToFormData';
import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";

function ProductUpload(){
    const SUCCESS_MESSAGE_TIME_IN_SECONDS = 2;


    const isAccessible = useVerifyUserAccess('/verification/product/upload')
    const [user] = useVerifyUserRole("ROLE_SELLER");
    const [product, setProduct] = useState();
    const [details, setDetails] = useState();
    const [images, setImages] = useState([]);

    const [selectedCategoryId, setSelectedCaegoryId] = useState();
    const [selectedSubcategoryId, setSelectedSubcategoryId] = useState();

    const [error, setError] = useState();
    const [successUploadCount, setSuccessUploadCount] = useState(0);
    const [successMessage, setSuccessMessage] = useState('');


    const navigate = useNavigate();


    useEffect(() => {
        setProduct({
            productName: '',
            brand: '',
            price: '',
            shippingPrice: '',
            quantity: '',
            deliveryTimeInDay: '',
            description: '',
        })
    }, [navigate]);

    useEffect(() => {
        if(successUploadCount){
            let currTime = SUCCESS_MESSAGE_TIME_IN_SECONDS;
            setSuccessMessage('The upload of the product was successfull!');
            const interval = setInterval(() => {
                if(currTime === 1){
                    clearInterval(interval);
                    setSuccessMessage('');
                }
                currTime--;
            }, 1000);
        }
    }, [successUploadCount]);


    function handleProductChange(value, field){
        const newDetail = {
            ...product
        }
        newDetail[field] = value;

        setProduct(newDetail);
    }

    function handleDetailChange(value, detailId){
        const changedDetails = changeProductDetail(value, detailId, details);
        setDetails(changedDetails);
    }

    function handleImagesChange(e){
        const uploadedFiles = e.target.files;
        const files = imageUpload(uploadedFiles, images);

        setImages(prev => [...prev, ...files]);
    }
    //Image deletion by Id or By name
    function handleImageDeletion(index){
        const deletedItem = images.filter((_, i) => i !== index);
        setImages(deletedItem);
    }

    async function handleSubmit(e){
        e.preventDefault();
        
        
        const convertedDetails = details ? detailsConverter(details) : [];
        const errorFields = [
            ...formDataValidator(product),
            ...formDataValidator({
                Category: selectedCategoryId,
                Subcategory: selectedSubcategoryId
            }),
            ...formDataValidator(convertedDetails)
        ].join(', ');

        if(isCorrectForm(errorFields)){
            const formData = createFormData({...product, subcategoryId: selectedSubcategoryId}, convertedDetails);
            const uploadProductResponse = await fetchPostAuthorizationFetch('/api/product', user.jwt, formData, false);
    
            if(uploadProductResponse.ok){
                setProduct({
                    productName: '',
                    brand: '',
                    price: '',
                    shippingPrice: '',
                    quantity: '',
                    deliveryTimeInDay: '',
                    description: '',
                });
                setSelectedCaegoryId('');
                setSelectedSubcategoryId('');
                setDetails();
                setImages([]);
                setSuccessUploadCount(prev => prev + 1);
            } else{
                const uploadResponse = await uploadProductResponse.json();
                setError(uploadResponse.errorMessage);
            }
        }

    }

    function createFormData(product, details){
        debugger;
        const formData = appendFilesToFormData('images', images);
        formData.append('productDetail', JSON.stringify(product));
        formData.append('productInformation', JSON.stringify(details));
        formData.append('userId', user.id);

        return(formData)
    }


    function isCorrectForm(errorFields){
        if(errorFields.length > 0){
            const errors = `Missing value at: ${errorFields}!`;
            setError(errors);
            return false;
        } else{
            setError();
            return true;
        }
    }


    function detailsConverter(productDetails){
        const convertedProductDetails = {};
        productDetails.forEach(productDetail => convertedProductDetails[productDetail.detailName] = productDetail.value);
        return convertedProductDetails;
    }
    

    function formDataValidator(information){
        const missingFields = [];
        for(const informationKey in information){
            if(!information[informationKey]){
                missingFields.push(informationKey);
            }
        }

        return missingFields;
    }

    return(
        <div className="product-upload">
            {(isAccessible && user) && 
                <>
                    {successMessage && <h3 className="success-message">{successMessage}</h3>}
                    <ProductInformationForm onDetailsChange={handleProductChange} productDetail={product}/>
                    <ProductCategoryForm 
                        onSelectCategoryIdChange={(categoryId) => setSelectedCaegoryId(categoryId)} 
                        onSelectedSubCategoryIdChange={(subcategoryId) => setSelectedSubcategoryId(subcategoryId)} 
                        selectedCategoryId={selectedCategoryId}
                        selectedSubcategoryId={selectedSubcategoryId}
                    />
                    {selectedSubcategoryId &&
                        <ProductDetailsForm 
                            details={details}
                            onDetailsChange={handleDetailChange}
                            subCategoryId={Number(selectedSubcategoryId)} 
                            user={user}
                            onDetailsSet={(details) => setDetails(details)}
                            onImageUpload={handleImagesChange}
                        />
                    }
                    {images.length ? <UploadedImages onImageDeletion={handleImageDeletion} images={images}/> : null}
                    <p className="error-message">{error}</p>
                    <button className="submit-button" onClick={handleSubmit} type="button" >Upload product!</button>
                </>
            }
        </div>
    )
}

export default ProductUpload;