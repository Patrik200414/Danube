import PropTypes from 'prop-types';

function ItemTable({renderElement}){
    return(
        <table className="product-detail-table">
            <tbody>
                {renderElement()}
            </tbody>
        </table>
    )
}

ItemTable.propTypes = {
    renderElement: PropTypes.func
}

export default ItemTable;