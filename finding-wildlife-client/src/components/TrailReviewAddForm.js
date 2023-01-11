import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import ParkSelection from "./ParkSelection";
import Trail from "./Trail";
import Error from "./Error";
import UserContext from "../UserContext";

const DEFAULT_REVIEW = { trailId: '',rating: '', difficulty: '', comments: '', appUserId: '' }

function TrailReviewForm() {

    const [trailReview, setTrailReview] = useState(DEFAULT_REVIEW);
    const [parks, setParks] = useState([]);
    const [trails, setTrails] = useState([]);
    const [errors, setErrors] = useState([])

    const history = useHistory();
    const { editId } = useParams();
    const authManager = useContext(UserContext);

    useEffect(() => {
        if(editId) {
            fetch(`http://localhost:8080/finding-wildlife/trail-review/${editId}`)
            .then(response => {
                switch(response.status){
                    case 200:
                        return response.json();
                    case 404:
                        history.push('/not-found', {id: editId})
                        break;
                    default:
                        return Promise.reject('Something Went Wrong!');
                }
            })
            .then(body => {
                if(body){
                    setTrailReview(body);
                }
            })
            .catch(error => history.push('/error', {errorMessage: error}));
        }
    }, []);

    useEffect(() => {
        fetch('http://localhost:8080/finding-wildlife/park')
        .then(response => {
          if(response.status === 200) {
            return response.json();
          }
          return Promise.reject('Something Went Wrong!');
        })
        .then(data => {
          setParks(data);
        })
        .catch(error => history.push('/error', {errorMessage: error}));
      }, []);


    function handleParkChange(event){
    fetch(`http://localhost:8080/finding-wildlife/park/${event.target.value}`)
    .then(response => {
        if(response.status === 200) {
        return response.json();
        }
        return Promise.reject('Something Went Wrong');
    })
    .then(data => {
        setTrails([...data.trails])
    })
    .catch(error => history.push('/error', {errorMessage: error}));
    }

    function updateTrailReview(){
        const updateTrailReview = {id: editId, ...trailReview};

        const init = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${authManager.user.token}`
            },
            body: JSON.stringify(updateTrailReview)
        };

        fetch(`http://localhost:8080/finding-wildlife/trail-review/${editId}`)
        .then(response => {
            switch(response.status) {
                case 204:
                    return null;
                case 400:
                    return response.json();
                case 404:
                    history.push('/not-found', { id: editId} );
                    break;
                default:
                    return Promise.reject('Something Went Wrong!');
            }
        })
        .then(body => {
            if(!body) {
                history.push('/trail-reviews')
            }
            else if(body){
                setErrors(body)
            }
        })
        .catch(error => history.push('/error', {errorMessage: error}));
    }

    function addTrailReview(){
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${authManager.user.token}`
            },
            body: JSON.stringify({...trailReview, appUserId: authManager.user.appUserId})
        };

        fetch('http://localhost:8080/finding-wildlife/trail-review', init)
        .then(response => {
            if(response.status === 201 || response.status === 400){
                return response.json();
            }
            return Promise.reject('Something Went Wrong!');
        })
        .then(body => {
            if(body.trailReviewId){
                history.push('/trail-reviews')
            }
            else if(body){
                setErrors(body);
            }
        })
        .catch(error => history.push('/error', {errorMessage: error}));
    }


    function onSubmit(event){
        event.preventDefault();

        const fetchFunction = editId > 0 ? updateTrailReview : addTrailReview;

        fetchFunction();
    }

    function handleChange(event){
        const property = event.target.name;
        const value = event.target.value;

        const newTrailReview = {...trailReview};

        newTrailReview[property] = value;
        setTrailReview(newTrailReview);
    }

    function handleCancel(){
        history.push('/trail-reviews')
    }

    return(<>
        <div className="add-review-container">
        <h2 className="text-center">{editId ? 'Update' : 'Add'} Trail Review</h2>
        {errors.length > 0 ? <Error errors={errors}/> : null}
        <form onSubmit={onSubmit}>
        <div className='form-group text-center'>
          <select className="form-control-sm mb-3" onChange={handleParkChange}>
            <option value="0" >Select National Park</option>
                {parks.map(park => <ParkSelection key={park.parkId} park={park}/>)}
            </select>
          </div>
            <div className='form-group text-center'>
                <select className="form-control-sm mb-3" name="trailId" onChange={handleChange}>
                <option value="0" >Select a Trail</option>
                    {trails && trails.map(t => {
                        return <Trail key={t.trailId} trail={t}/>})}
                </select>
            </div>
            <div className="form-group">
                <label htmlFor="rating">Rating [1-5]</label>
                <input name="rating" type="number" className="form-control" id="rating" value={trailReview.rating} onChange={handleChange}/>
            </div>
            <div className="form-group">
                <label htmlFor="difficulty">Difficulty [1-5]</label>
                <input name="difficulty" type="number" className="form-control" id="difficulty" value={trailReview.difficulty} onChange={handleChange}/>
            </div>
            <div className="form-group">
                <label htmlFor="comments">Comments</label>
                <input name="comments" type="text" className="form-control mb-3" id="comments" value={trailReview.comments} onChange={handleChange}/>
            </div>
            <div className="form-group text-center">
                <button type="submit" className="btn btn-success mr-3">Submit</button>
                <button type="button" className="btn btn-danger ml-3" onClick={handleCancel}>Cancel</button>
            </div>
        </form>
        </div>
    </>);
}

export default TrailReviewForm;