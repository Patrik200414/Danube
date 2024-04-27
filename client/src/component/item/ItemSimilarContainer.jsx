import ProductCard from "../product/ProductCard";
import Proptypes from 'prop-types'

function ItemSimularContainer({similarProducts}){
    return(
        <div className="uploaded-images-container">
            {similarProducts.map(similarProduct => <ProductCard key={similarProduct.id} product={similarProduct}/>)}
        </div>
    )
}

ItemSimularContainer.propTypes = {
    similarProducts: Proptypes.array
}

export default ItemSimularContainer;