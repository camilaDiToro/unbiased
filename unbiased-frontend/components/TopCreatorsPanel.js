import Link from "next/link";
import {useAppContext} from "../context";
import PropTypes, {shape} from "prop-types";

export default function TopCreatorsPanel(props) {
    const {I18n} = useAppContext()
    return <div
        className="card container w-100 w-xl-25 p-4 h-auto m-2 h-fit align-self-xl-start"
        id="none_shadow"
    >
        <h5
            style={{ backgroundImage: "url('/img/crown-svgrepo-com.svg')" }}
            className="card-title top-creators"
        >
            {I18n("home.topCreators")}
        </h5>

        {props.creators.length === 0 ? (
            <h6 className="text-info m-1">{I18n("home.emptyCreators")}</h6>
        ) : (
            <></>
        )}
        {props.creators.map((c) => (
            <Link key={c.id} className="m-1 link" href={`/profile/${c.id}`}>
                <div
                    className="card text-white d-flex flex-row p-2 creator-card align-items-center"
                    id="none_shadow_creator"
                >
                    <div className="img-container">
                        <img
                            className="rounded-circle object-fit-cover mr-1"
                            src="/img/profile-image.png"
                            alt=""
                        />
                    </div>
                    <div className="mx-2 text-ellipsis-1">{c.nameOrEmail}</div>
                </div>
            </Link>
        ))}
    </div>
}

TopCreatorsPanel.propTypes = {
    creators: PropTypes.arrayOf(PropTypes.shape({nameOrEmail: PropTypes.string.isRequired, hasImage: PropTypes.bool.isRequired, id: PropTypes.number.isRequired}))
}