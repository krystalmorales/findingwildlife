import StarRatings from 'react-star-ratings';

function StarRating({ rating }){

    return(<>
    <div className='stars'>
        <StarRatings
            rating={rating}
            starDimension="20px"
            starSpacing="2px"
            starRatedColor='gold'
            starEmptyColor='grey'
        />
        </div>
    </>);
}

export default StarRating;
