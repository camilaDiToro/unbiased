import Link from "next/link";
import {useRouter} from "next/router";
import types from "../types";

export default function CancelSearchLink(props) {
    const router = useRouter()
    return <div className="m-3 ">
        <Link className="link" href={{
            pathname: "/",
            query: { order: router.query.order, type: router.query.type },
        }}>
            <div className="link-text">
                {props.text}
            </div>
        </Link>
    </div>
}

CancelSearchLink.propTypes = types.CancelSearchLink