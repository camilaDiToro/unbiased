import Link from "next/link";
import {useRouter} from "next/router";
import types from "../types";

export default function CancelSearchLink(props) {
    const router = useRouter()
    return <div className="m-3 ">
        <Link className="link" href={{
            pathname: "/",
            query: { ...router.query.order },
        }}>
            <div className="link-text">
                {props.text}
            </div>
        </Link>
    </div>
}

CancelSearchLink.propTypes = types.CancelSearchLink